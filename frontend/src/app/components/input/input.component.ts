import { Component } from '@angular/core';
import {ClrCommonFormsModule, ClrInputModule, ClrTextareaModule} from '@clr/angular';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-input',
  imports: [
    ClrCommonFormsModule,
    ClrInputModule,
    FormsModule,
    ClrTextareaModule
  ],
  templateUrl: './input.component.html',
  standalone: true,
  styleUrl: './input.component.scss'
})
export class InputComponent {
    input:String=""

  autoResize(event: Event) {
    const textarea = event.target as HTMLTextAreaElement;
    textarea.style.height = 'auto';
    textarea.style.height = textarea.scrollHeight + 'px';
  }
}
