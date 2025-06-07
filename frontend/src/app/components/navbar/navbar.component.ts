import { Component } from '@angular/core';
import {ClrIconModule} from '@clr/angular';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-navbar',
  imports: [
    ClrIconModule,
    RouterLink
  ],
  templateUrl: './navbar.component.html',
  standalone: true,
  styleUrl: './navbar.component.scss'
})
export class NavbarComponent {

}
