import { Component } from '@angular/core';
import {ClrIconModule} from "@clr/angular";
import {ClarityIcons, fileIcon, vmBugIcon, wrenchIcon} from '@cds/core/icon';
import '@cds/core/icon/register.js';
ClarityIcons.addIcons(fileIcon);

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
  currentYear: number = new Date().getFullYear();
}
