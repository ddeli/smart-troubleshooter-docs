import {Component, inject} from '@angular/core';
import {DataService} from '../../services/data.service';
import {JsonPipe, NgForOf, NgIf} from '@angular/common';

@Component({
  selector: 'app-export',
  imports: [
    JsonPipe,
    NgIf,
    NgForOf
  ],
  templateUrl: './export.component.html',
  standalone: true,
  styleUrl: './export.component.scss'
})
export class ExportComponent {
  private dataService = inject(DataService);

  responseData: any = null;
  showRawJson = false;

  loadData() {
    this.dataService.getData().subscribe(data => {
      this.responseData = data;
      console.log('API Response:', data);
    });
  }
}
