import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TrenDetailComponent } from './tren-detail.component';

describe('Component Tests', () => {
  describe('Tren Management Detail Component', () => {
    let comp: TrenDetailComponent;
    let fixture: ComponentFixture<TrenDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [TrenDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ tren: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(TrenDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TrenDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load tren on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.tren).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
