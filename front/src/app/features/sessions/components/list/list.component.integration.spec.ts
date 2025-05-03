import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ListComponent } from './list.component';
import { SessionApiService } from '../../services/session-api.service';
import { SessionService } from '../../../../services/session.service';
import { Session } from '../../interfaces/session.interface';
import { SessionInformation } from '../../../../interfaces/sessionInformation.interface';

describe('ListComponent [INTÉGRATION]', () => {
  let component: ListComponent;
  let fixture: ComponentFixture<ListComponent>;

  // Stub des sessions renvoyées par l'API
  const fakeSessions: Session[] = [
    {
      id: 1,
      name: 'Session 1',
      description: 'Description 1',
      date: new Date('2025-01-01T10:00:00'),
      teacher_id: 1,
      users: [],
      createdAt: new Date(),
      updatedAt: new Date(),
    },
    {
      id: 2,
      name: 'Session 2',
      description: 'Description 2',
      date: new Date('2025-02-01T10:00:00'),
      teacher_id: 2,
      users: [1],
      createdAt: new Date(),
      updatedAt: new Date(),
    }
  ];

  // Stub de l'information de session (utilisateur courant)
  const fakeInfo: SessionInformation = {
    id: 123,
    username: 'testuser',
    token: 'fake-token',
    type: 'Bearer',
    admin: true,
    firstName: 'Test',
    lastName: 'User'
  };

  // Stub du service API
  const fakeApi = {
    all: () => of(fakeSessions)
  };

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      imports: [
        MatCardModule,
        RouterTestingModule.withRoutes([])
      ],
      declarations: [ ListComponent ],
      providers: [
        { provide: SessionApiService, useValue: fakeApi },
        { provide: SessionService,    useValue: { sessionInformation: fakeInfo } }
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('doit créer le composant', () => {
    expect(component).toBeTruthy();
  });

  it('doit afficher une carte par session retournée', () => {
    const cards = fixture.nativeElement.querySelectorAll('mat-card.item');
    expect(cards.length).toBe(fakeSessions.length);
  });
});
