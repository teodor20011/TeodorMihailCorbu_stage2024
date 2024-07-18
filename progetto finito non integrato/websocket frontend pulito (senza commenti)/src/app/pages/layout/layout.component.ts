import { Component } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { Login } from '../login/login.component';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { catchError, of, switchMap } from 'rxjs';
import { ChatService } from '../../service/chat.service';

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [RouterOutlet, FormsModule, HttpClientModule],
  templateUrl: './layout.component.html',
  styleUrl: './layout.component.css'
})
export class LayoutComponent {

  nickname = sessionStorage.getItem('nickname');
  logoutObj: Login;

  constructor(private http: HttpClient, private router: Router, private chatService: ChatService) {
    this.logoutObj = new Login();
  }

  logout() {
    debugger
    if (this.nickname != null) {
      debugger
      this.http.get<any>(`http://localhost:8080/user/${this.nickname}`).pipe(
        catchError((error) => {
          console.error('Error during logout request:', error);
          return of(null);
        }),
        switchMap((res: any) => {
          debugger
          console.log(res); // Controlla cosa contiene res
          debugger
          console.log(this.logoutObj);
          
          debugger
          if (res) {
            this.logoutObj.nickname = res.nickname;
            debugger
            this.logoutObj.email = res.email;
            debugger
            this.logoutObj.password = res.password;
            debugger
            this.logoutObj.isLoggedIn = res.isLoggedIn;
            debugger
            console.log(this.logoutObj);
            debugger
            this.logoutObj.isLoggedIn = false;
            debugger
            console.log(this.logoutObj);
            debugger
            // Effettua la chiamata PUT per aggiornare l'utente
            return this.http.put('http://localhost:8080/user', this.logoutObj).pipe(
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
        debugger
        if (updateRes) {
          debugger
          sessionStorage.clear();
          debugger
          console.log('User updated successfully:', updateRes);
          debugger
          
          // Disconnessione WebSocket
          this.chatService.disconnectWebSocket();
          
          this.router.navigateByUrl('/login');
        }
      });
    } else {
      alert('Errore durante il caricamento dell\'utente');
    }
  }
}
