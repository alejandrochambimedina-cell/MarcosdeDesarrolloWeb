import { TestBed } from '@angular/core/testing';
import { AppComponent } from './app.component'; // 1. Corregido: Importa AppComponent
import { appConfig } from './app.config';
import { provideRouter } from '@angular/router';

describe('AppComponent', () => { // Cambiado el nombre del bloque de pruebas
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AppComponent], // 2. Corregido: Usa el componente correcto
      providers: [
        ...(appConfig.providers || []) // Evita fallos si providers no está definido
      ],
    }).compileComponents();
  });

  it('crea la estructura principal', () => {
    const fixture = TestBed.createComponent(AppComponent); // 3. Corregido
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it('ofrece tres enlaces y un contenedor de rutas', async () => {
    const fixture = TestBed.createComponent(AppComponent); // 4. Corregido
    fixture.detectChanges(); // Importante: Fuerza a Angular a renderizar el HTML antes de buscar elementos
    
    await fixture.whenStable();
    const compiled = fixture.nativeElement as HTMLElement;
    
    // Comprobaciones de la estructura HTML
    expect(compiled.querySelectorAll('nav a').length).toBe(3);
    expect(compiled.querySelector('router-outlet')).not.toBeNull();
  });
});
