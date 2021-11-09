import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { TiketService } from '../service/tiket.service';

import { TiketComponent } from './tiket.component';

describe('Component Tests', () => {
  describe('Tiket Management Component', () => {
    let comp: TiketComponent;
    let fixture: ComponentFixture<TiketComponent>;
    let service: TiketService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TiketComponent],
      })
        .overrideTemplate(TiketComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TiketComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(TiketService);

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
      expect(comp.tikets?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
