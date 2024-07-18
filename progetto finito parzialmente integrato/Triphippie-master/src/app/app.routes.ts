import { Routes } from '@angular/router';
import { LoginComponent } from './componenti/login/login.component';
import { RegistrazioneComponent } from './componenti/registrazione/registrazione.component';
import { NotFoundComponent } from './componenti/not-found/not-found.component';
import { DashboardComponent } from './componenti/dashboard/dashboard.component';
//modificato qui
import { LayoutComponent } from './componenti/layout/layout.component';
//modificato qui
import { ChatComponent } from './componenti/chat/chat.component';
import { AuthGuard } from './servizi/auth.guard';
import { ProfiloComponent } from './componenti/profilo/profilo.component';
import { ProfiloModificaComponent } from './componenti/profilo/profilo-modifica/profilo-modifica.component';
import { HomepageComponent } from './componenti/homepage/homepage.component';
import { AreaRiservataComponent } from './componenti/area-riservata/area-riservata.component';
import { CreaViaggioComponent } from './componenti/crea-viaggio/crea-viaggio.component';
import { IMieiViaggiComponent } from './componenti/i-miei-viaggi/i-miei-viaggi.component';
import { IMieiViaggiModificaComponent } from './componenti/i-miei-viaggi/i-miei-viaggi-modifica/i-miei-viaggi-modifica.component';
import { TripDetailsComponent } from './componenti/dashboard/trip-details/trip-details.component';

export const routes: Routes = [
    {path: '', redirectTo:'home' , pathMatch:'full'},
    {path:'home', component:HomepageComponent},
    {path:'login', component:LoginComponent},
    {path:'registrazione', component:RegistrazioneComponent},
    {path:'dashboard', component:DashboardComponent, canActivate: [AuthGuard]},

    //modificato qui
    {path:'layout', component:LayoutComponent, canActivate: [AuthGuard], children:[
        {path:'', component:ChatComponent, canActivate: [AuthGuard] },
    ]},
    {path:'trip-details', component:TripDetailsComponent, canActivate: [AuthGuard] },
    {path:'area-riservata', component:AreaRiservataComponent , canActivate: [AuthGuard], children:[
        {path:'', component:ProfiloComponent, canActivate: [AuthGuard] },
        {path:'profilo', component:ProfiloComponent, canActivate: [AuthGuard] },
        {path:'modifica-profilo', component:ProfiloModificaComponent, canActivate: [AuthGuard] },
        {path:'crea-viaggio', component:CreaViaggioComponent, canActivate: [AuthGuard] },
        {path:'i-miei-viaggi', component:IMieiViaggiComponent, canActivate: [AuthGuard] },
        {path:'modifica-viaggio', component:IMieiViaggiModificaComponent, canActivate: [AuthGuard] },
    ]},
    {path: '**', component:NotFoundComponent},
];
