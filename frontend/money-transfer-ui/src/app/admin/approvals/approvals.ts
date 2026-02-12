import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { API } from '../../core/api';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-approvals',
  standalone: true,
  imports: [RouterModule, CommonModule],
  templateUrl: './approvals.html'
})
export class Approvals implements OnInit {

  accounts: any[] = [];

  constructor(
    private http: HttpClient,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadPending();
  }

  loading = false;
  message = '';
  messageType = '';

  loadPending(): void {
    this.loading = true;

    this.http.get<any[]>(API.ADMIN.PENDING)
      .subscribe({
        next: (data) => {
          this.accounts = data;
          this.loading = false;
          this.cdr.detectChanges();
        },
        error: (err) => {
          console.error('Failed to load pending accounts:', err);
          this.message = 'Failed to load pending accounts';
          this.messageType = 'error';
          this.loading = false;
          this.cdr.detectChanges();
        }
      });
  }

  approve(id: number): void {

    if (this.loading) return;
    this.loading = true;

    this.http.post(API.ADMIN.APPROVE(id), {})
      .subscribe({
        next: () => {
          this.message = 'Account approved successfully';
          this.messageType = 'success';
          this.loadPending();   // reload list
        },
        error: (err) => {
          this.message = err?.error?.message || 'Approval failed';
          this.messageType = 'error';
          this.loading = false;
          this.cdr.detectChanges();
        }
      });
  }


  reject(id: number): void {

    if (this.loading) return;

    this.loading = true;

    this.http.post(API.ADMIN.REJECT(id), {})
      .subscribe({
        next: () => {
          this.message = 'Account rejected';
          this.messageType = 'success';
          this.loadPending();
        },
        error: (err) => {
          this.message = err?.error?.message || 'Reject failed';
          this.messageType = 'error';
          this.loading = false;
          this.cdr.detectChanges();
        }
      });
  }

}