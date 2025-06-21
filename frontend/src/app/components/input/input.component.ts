import {Component, EventEmitter, inject, Output} from '@angular/core';
import {ClrCommonFormsModule, ClrInputModule, ClrTextareaModule} from '@clr/angular';
import {FormsModule} from '@angular/forms';
import { HttpClientModule, HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-input',
  imports: [
    ClrCommonFormsModule,
    ClrInputModule,
    FormsModule,
    ClrTextareaModule,
    HttpClientModule
  ],
  templateUrl: './input.component.html',
  standalone: true,
  styleUrl: './input.component.scss'
})
export class InputComponent {

  @Output() inputTextSubmit = new EventEmitter<string>();

  inputText: string = '';

  autoResize(event: Event) {
    const textarea = event.target as HTMLTextAreaElement;
    textarea.style.height = 'auto';
    textarea.style.height = textarea.scrollHeight + 'px';
  }

  submitText() {
    this.inputTextSubmit.emit(this.inputText);
  }

}
