import { TestBed } from '@angular/core/testing';
import { HttpClientModule } from '@angular/common/http';
import {
  HttpClientInMemoryWebApiModule,
  InMemoryDbService
} from 'angular-in-memory-web-api';
import { SessionApiService } from './session-api.service';
import { Session } from '../interfaces/session.interface';

/** 
 * On définit un petit fake-backend InMemory pour renvoyer
 * quelques sessions « en dur » 
 */
class FakeSessionsDbService implements InMemoryDbService {
  createDb() {
    const sessions: Session[] = [
      { id: 42, name: 'Intégration', description: 'desc', date: new Date('2025-12-05'), teacher_id: 1, users: [], createdAt: new Date(), updatedAt: new Date() }
    ];
    return { session: sessions };
  }
}

describe('SessionApiService [INTÉGRATION]', () => {
  let service: SessionApiService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        // on utilise l’in-memory-web-api pour simuler une vraie API REST
        HttpClientInMemoryWebApiModule.forRoot(FakeSessionsDbService, {
          apiBase: 'api/',
          passThruUnknownUrl: true
        })
      ],
      providers: [SessionApiService]
    });
    service = TestBed.inject(SessionApiService);
  });

  it('doit récupérer la liste complète des sessions', (done) => {
    service.all().subscribe(sessions => {
      expect(sessions.length).toBe(1);
      expect(sessions[0].id).toBe(42);
      expect(sessions[0].name).toContain('Intégration');
      done();
    });
  });

  it('doit récupérer le détail d’une session par son id', (done) => {
    service.detail('42').subscribe(sess => {
      expect(sess).toBeTruthy();
      expect(sess.id).toBe(42);
      done();
    });
  });

  // tu peux ajouter create/update/delete de la même façon...
});
