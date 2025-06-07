import { Component } from '@angular/core';
import {ClrIconModule} from "@clr/angular";

@Component({
  selector: 'app-footer',
  imports: [
    ClrIconModule
  ],
  templateUrl: './footer.component.html',
  standalone: true,
  styleUrl: './footer.component.scss'
})
export class FooterComponent {

}
