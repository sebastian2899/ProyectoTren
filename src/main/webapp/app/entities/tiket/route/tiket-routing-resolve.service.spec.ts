jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ITiket, Tiket } from '../tiket.model';
import { TiketService } from '../service/tiket.service';

import { TiketRoutingResolveService } from './tiket-routing-resolve.service';

describe('Tiket routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: TiketRoutingResolveService;
  let service: TiketService;
  let resultTiket: ITiket | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(TiketRoutingResolveService);
    service = TestBed.inject(TiketService);
    resultTiket = undefined;
  });

  describe('resolve', () => {
    it('should return ITiket returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTiket = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTiket).toEqual({ id: 123 });
    });

    it('should return new ITiket if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTiket = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultTiket).toEqual(new Tiket());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Tiket })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTiket = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTiket).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
