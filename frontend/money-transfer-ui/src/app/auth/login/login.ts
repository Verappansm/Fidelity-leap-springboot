import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router, RouterModule } from '@angular/router';
import { API } from '../../core/api';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './login.html'
})
export class Login {

  formData = {
    email: '',
    password: ''
  };

  message = '';
  messageType = '';

  constructor(
    private http: HttpClient,
    private router: Router
  ) {}

  onLogin(): void {

    
this.http.post<any>(API.AUTH.LOGIN, this.formData)
      .subscribe({
        next: (data) => {

          localStorage.setItem('token', data.token);
          localStorage.setItem('accountId', data.accountId);
          localStorage.setItem('holderName', data.holderName);
          localStorage.setItem('email', data.email);
          localStorage.setItem('role', data.role);
          localStorage.setItem('balance', data.balance);

          if (data.role === 'ROLE_ADMIN') {
            this.router.navigate(['/admin']);
          } else {
            this.router.navigate(['/dashboard']);
          }
        },

        error: (err) => {
          const msg =
            err?.error?.message ||
            'Login failed. Please check your credentials.';
          this.showMessage(msg, 'error');
        }
      });
  }

  showMessage(msg: string, type: string): void {
    this.message = msg;
    this.messageType = type;
  }
}