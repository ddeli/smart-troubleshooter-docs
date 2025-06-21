import {Component, EventEmitter, Input, Output} from '@angular/core';
import {ClrSpinnerModule} from '@clr/angular';
import {NgIf} from '@angular/common';

@Component({
  selector: 'app-spinner',
  imports: [
    ClrSpinnerModule,
    NgIf
  ],
  templateUrl: './spinner.component.html',
  standalone: true,
  styleUrls: ['./spinner.component.scss']
})
export class SpinnerComponent {
  fetchingUserInformation = false;

  @Input() isLoading: boolean = false;
  @Output() fetchingFinished = new EventEmitter<void>();
  @Output() buttonPressed = new EventEmitter<void>();

  toggleUserInfo() {
    this.fetchingUserInformation = !this.fetchingUserInformation;
    this.buttonPressed.emit();
    if(this.isLoading) {
      this.fetchingUserInformation = false;
      this.fetchingFinished.emit();
    }
  }

  // onClick() {
  //   this.buttonPressed.emit();
  // }
}
