import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { App } from './app/app';

bootstrapApplication(App, appConfig)
  .catch((err) => console.error(err));


// import { bootstrapApplication } from '@angular/platform-browser';
// import { App } from './app/app';
// import { provideHttpClient, withInterceptors } from '@angular/common/http';

// bootstrapApplication(App, {
//   providers: [
//     provideHttpClient(
//       withInterceptors([
//         (req, next) => {

//           // skip if already full URL
//           if (req.url.startsWith('http')) {
//             return next(req);
//           }

//           const newReq = req.clone({
//             url: 'http://54.144.82.66:8080' + req.url
//           });

//           return next(newReq);
//         }
//       ])
//     )
//   ]
// });