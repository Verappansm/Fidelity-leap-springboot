import { ChangeDetectorRef, Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Router, RouterModule } from '@angular/router';
import { API } from '../../core/api';
import { ValidationError } from '../../core/models/validation-error';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [RouterModule, CommonModule, FormsModule],
  templateUrl: './register.html'
})
export class Register {

  formData = {
    holderName: '',
    email: '',
    password: ''
  };

  message = '';
  messageType = '';

  loading = false;

  constructor(
    private http: HttpClient,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  onRegister(): void {
    console.log('Registering user with data:', this.formData);

    if (this.loading) return;

    this.loading = true;

    this.http.post(API.AUTH.REGISTER, this.formData)
      .subscribe({
        next: () => {
          this.showMessage(
            'Registration successful! Your account is pending admin approval. You will be able to login once approved, and will be redirected to the login page shortly.',
            'success'
          );

          this.formData = {
            holderName: '',
            email: '',
            password: ''
          };

          setTimeout(() => {
            this.router.navigate(['/login']);
          }, 3000);
        },

       error: (err: HttpErrorResponse) => {

        const errorBody = err.error as ValidationError;

        if (errorBody?.errors) {
          const validationMessages =
            Object.values(errorBody.errors).join(', ');
          this.showMessage(validationMessages, 'error');
        } else {
          this.showMessage(
            errorBody?.message || 'Registration failed',
            'error'
          );
        }
      }

      });
  }

  showMessage(msg: string, type: string): void {
    this.message = msg;
    this.messageType = type;
    this.cdr.detectChanges();
  }
}