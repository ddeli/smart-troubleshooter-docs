import { Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { AboutComponent } from './pages/about/about.component';
import {ExportComponent} from './pages/export/export.component';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'export', component: ExportComponent },
  { path: 'about', component: AboutComponent }
];
