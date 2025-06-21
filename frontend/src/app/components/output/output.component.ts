import {Component, Input} from '@angular/core';
import {ClrCommonFormsModule, ClrTextareaModule} from '@clr/angular';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-output',
  imports: [
    ClrCommonFormsModule,
    ClrTextareaModule,
    FormsModule
  ],
  templateUrl: './output.component.html',
  standalone: true,
  styleUrl: './output.component.scss'
})
export class OutputComponent {



  @Input() documentation = {
    title: '',
    symptom: '',
    problem: '',
    solution: ''
  };


  autoResize(event: Event) {
    const textarea = event.target as HTMLTextAreaElement;
    textarea.style.height = 'auto';
    textarea.style.height = textarea.scrollHeight + 'px';
  }
}
