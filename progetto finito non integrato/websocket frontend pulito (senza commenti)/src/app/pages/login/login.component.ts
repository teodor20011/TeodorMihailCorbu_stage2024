import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { catchError, of, switchMap } from 'rxjs';
import { ChatService } from '../../service/chat.service';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, HttpClientModule, ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  loginObj: Login;

  constructor(private http: HttpClient, private router: Router, private chatService: ChatService) {
    this.loginObj = new Login();
  }

  onLogin() {
    this.http.post<any>('http://localhost:8080/user/login', this.loginObj).pipe(
      catchError((error) => {
          console.error('Error during login request:', error);
          if (error.status === 401 && error.error && error.error.message) {
              alert(error.error.message);
          } else {
              alert('Errore durante la richiesta di login. Si prega di riprovare più tardi.');
          }
          return of(null);
      }),
      switchMap((res: any) => {
          console.log(res); // Controlla cosa contiene res
          console.log(this.loginObj);
          if (res && res.success) {
              alert('Login Success');

              sessionStorage.setItem('angular17token', res.token);
              sessionStorage.setItem('nickname', res.nickname);

              this.loginObj.isLoggedIn = true;
              console.log(this.loginObj);

              this.chatService.connectWebSocket(() => {
                console.log('WebSocket connected for the logged-in user.');
              });

              // Effettua la chiamata PUT per aggiornare l'utente
              return this.http.put('http://localhost:8080/user', this.loginObj).pipe(
                  catchError((updateError) => {
                      console.error('Error updating user:', updateError);
                      alert('Errore durante l\'aggiornamento dell\'utente. Si prega di riprovare più tardi.');
                      return of(null);
                  })
              );
          } else if (res) {
              alert(res.message);
              return of(null);
          } else {
              return of(null);
          }
      })
  ).subscribe((updateRes: any) => {
      if (updateRes) {
          console.log('User updated successfully:', updateRes);
          this.router.navigateByUrl('/dashboard');
      }
  });
  }
}



export class Login {
  nickname: string;
  email: string;
  password: string;
  isLoggedIn: boolean;
  constructor() {
    this.nickname = '';
    this.email = '';
    this.password = '';
    this.isLoggedIn = false;
  }
}
