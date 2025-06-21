import {Component, inject} from '@angular/core';
import {InputComponent} from '../../components/input/input.component';
import {SpinnerComponent} from '../../components/spinner/spinner.component';
import {OutputComponent} from '../../components/output/output.component';
import {NgIf} from '@angular/common';
import {HttpClient} from '@angular/common/http';

@Component({
  selector: 'app-home',
  imports: [InputComponent, SpinnerComponent, OutputComponent, NgIf],
  templateUrl: './home.component.html',
  standalone: true,
  styleUrl: './home.component.scss'
})
export class HomeComponent {


  inputText: string = '';

  documentation: {
    title: string;
    symptom: string;
    problem: string;
    solution: string;
  } | null = null;
  loading = false;
  http = inject(HttpClient);

  showOutput: boolean = false;

  onButtonPressed() {
    if (this.showOutput) {
      // this.showOutput = false;
    }

    this.sendRequest();
  }
  onFetchingFinished() {
    // this.showOutput = !this.showOutput;
  }

  sendRequest() {
    console.log("TEST")
    this.documentation = null;
    this.loading = true;
    this.showOutput = false;

    const body = { text: this.inputText };

    this.http.post<{ documentation: any }>('/generate-doc-adjust', body)
      .subscribe({
        next: response => {
          this.documentation = response.documentation;
          this.loading = false;  // Spinner aus
          this.showOutput = true;
        },
        error: err => {
          this.documentation = {
            title: '',
            symptom: '',
            problem: 'Fehler: ' + (err.message || err.statusText),
            solution: '',
          };
          this.loading = false;  // Spinner aus
          this.showOutput = true;
        }
      });
  }
}
