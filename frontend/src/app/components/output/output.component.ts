import {Component, inject, Input} from '@angular/core';
import {ClrCommonFormsModule, ClrTextareaModule} from '@clr/angular';
import {FormsModule} from '@angular/forms';
import {NgIf} from '@angular/common';
import {DataService} from '../../services/data.service';

@Component({
  selector: 'app-output',
  imports: [
    ClrCommonFormsModule,
    ClrTextareaModule,
    FormsModule,
    NgIf
  ],
  templateUrl: './output.component.html',
  standalone: true,
  styleUrl: './output.component.scss'
})
export class OutputComponent {

  private dataService = inject(DataService);

  @Input() documentation = {
    title: '',
    symptom: '',
    problem: '',
    solution: ''
  };

  saveArticle() {
    this.dataService.saveArticle(this.documentation).subscribe(
      response => {
        console.log('Article successfully saved', response);
        // Optional: Show success notification
        alert('Article successfully saved!');
      },
      error => {
        console.error('Error saving article', error);
        // Optional: Display error notification
        alert('Error saving article!');
      }
    );
  }


  autoResize(event: Event) {
    const textarea = event.target as HTMLTextAreaElement;
    textarea.style.height = 'auto';
    textarea.style.height = textarea.scrollHeight + 'px';
  }
}
