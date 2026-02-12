import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { RouterModule } from '@angular/router';
import { API } from '../../core/api';

@Component({
  selector: 'app-admin-accounts',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './accounts.html'
})
export class Accounts implements OnInit {

  accounts: any[] = [];

  constructor(
    private http: HttpClient,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadAccounts();
  }

  loadAccounts(): void {
    this.http.get<any[]>(API.ADMIN.ALL_ACCOUNTS)
      .subscribe({
        next: (data) => {
          this.accounts = data;
          this.cdr.detectChanges();
        },
        error: (err) => {
          console.error('Failed to load accounts:', err);
        }
      });
  }

  formatAmount(amount: number): string {
    return Number(amount).toFixed(2);
  }
}
