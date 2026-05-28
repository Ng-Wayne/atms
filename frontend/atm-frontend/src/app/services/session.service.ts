import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class SessionService {

  setSessionId(id: string) {
    localStorage.setItem('sessionId', id);
  }

  getSessionId(): string {
    return localStorage.getItem('sessionId') || '';
  }

  clearSessionId() {
    localStorage.removeItem('sessionId');
  }

}
