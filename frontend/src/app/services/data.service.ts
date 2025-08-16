import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class DataService {

  private http = inject(HttpClient);

  getData(): Observable<any> {
    return this.http.get(`${environment.apiUrl}/articles`);
  }

  setTestData(){
    return this.http.get(`${environment.apiUrl}/test-insert`);
  }

  saveArticle(articleData: any): Observable<any> {
    return this.http.post(`${environment.apiUrl}/save-article`, articleData);
  }
}
