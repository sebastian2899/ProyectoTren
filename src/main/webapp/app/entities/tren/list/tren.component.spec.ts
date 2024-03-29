import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { TrenService } from '../service/tren.service';

import { TrenComponent } from './tren.component';

describe('Component Tests', () => {
  describe('Tren Management Component', () => {
    let comp: TrenComponent;
    let fixture: ComponentFixture<TrenComponent>;
    let service: TrenService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TrenComponent],
      })
        .overrideTemplate(TrenComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TrenComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(TrenService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'query').mockReturnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.trens?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
