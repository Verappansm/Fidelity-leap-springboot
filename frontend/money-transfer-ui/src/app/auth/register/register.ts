import { Component, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router, RouterModule } from '@angular/router';
import { API } from '../../core/api';
import { FormService } from '../../core/services/form.service';

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

  confirmPassword = '';

  message = '';
  messageType = '';
  loading = false;

  constructor(
    private http: HttpClient,
    private router: Router,
    private cdr: ChangeDetectorRef,
    private form: FormService
  ) {}

  onRegister(form: any): void {

    if (this.loading) return;

    if (this.formData.password !== this.confirmPassword) {
      this.form.setError(this, null, 'Passwords do not match');
      this.cdr.detectChanges();
      return;
    }

    this.loading = true;

    this.http.post(API.AUTH.REGISTER, this.formData)
      .subscribe({
        next: () => {

          this.form.setSuccess(
            this,
            'Registration successful! Your account is pending admin approval. You will be redirected shortly.'
          );

          form.resetForm();
          this.confirmPassword = '';

          this.cdr.detectChanges();

          // Redirect to login after delay
          setTimeout(() => {
            this.router.navigate(['/login']);
          }, 3000);
        },

        error: (err) => {
          this.form.setError(this, err, 'Registration failed');
          this.cdr.detectChanges();
        }
      });
  }
}