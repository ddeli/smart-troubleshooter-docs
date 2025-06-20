import { Component } from '@angular/core';
import {InputComponent} from '../../components/input/input.component';
import {SpinnerComponent} from '../../components/spinner/spinner.component';
import {OutputComponent} from '../../components/output/output.component';
import {NgIf} from '@angular/common';

@Component({
  selector: 'app-home',
  imports: [InputComponent, SpinnerComponent, OutputComponent, NgIf],
  templateUrl: './home.component.html',
  standalone: true,
  styleUrl: './home.component.scss'
})
export class HomeComponent {
  showOutput: boolean = false;

  onButtonPressed() {
    if (this.showOutput) {
      this.showOutput = false;
    }
  }
  onFetchingFinished() {
    this.showOutput = !this.showOutput;
  }
}
