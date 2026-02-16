import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { ChangeDetectorRef } from '@angular/core';
import { API } from '../../core/api';
import { AuthService } from '../../core/services/auth.service';
import { BalanceService } from '../../core/services/balance.service';

@Component({
  selector: 'app-user-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './user-dashboard.html',
  styleUrl: './user-dashboard.css',
})
export class UserDashboard {
  holderName = '';
  accountId = '';
  balance = '0.00';

  constructor(
    private http: HttpClient,
    private router: Router,
    private auth: AuthService,
    private cdr: ChangeDetectorRef,
    private balanceService: BalanceService
  ) {}

  ngOnInit(): void {

    if (!this.auth.isAuthenticated()) {
      this.router.navigate(['/login']);
      return;
    }

    this.holderName = localStorage.getItem('holderName') || '';
    this.accountId = localStorage.getItem('accountId') || '';

    this.loadBalance();

    this.balanceService.balanceChanged$.subscribe(() => {
      this.loadBalance();
    });
  }

  loadBalance(): void {
  this.http.get<any>(
    API.ACCOUNTS.BALANCE(Number(this.accountId))
  ).subscribe({
      next: (data) => {
        this.balance = Number(data.balance).toFixed(2);
        console.log('Balance loaded:', this.balance);
        this.cdr.detectChanges(); 
      },
      error: (err) => {
        console.log(err);
      }
    });
  }


  logout() {
    this.auth.logout();
  }

}