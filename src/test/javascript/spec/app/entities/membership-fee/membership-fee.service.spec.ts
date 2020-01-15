import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { MembershipFeeService } from 'app/entities/membership-fee/membership-fee.service';
import { IMembershipFee, MembershipFee } from 'app/shared/model/membership-fee.model';

describe('Service Tests', () => {
  describe('MembershipFee Service', () => {
    let injector: TestBed;
    let service: MembershipFeeService;
    let httpMock: HttpTestingController;
    let elemDefault: IMembershipFee;
    let expectedResult: IMembershipFee | IMembershipFee[] | boolean | null;
    let currentDate: moment.Moment;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(MembershipFeeService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new MembershipFee(0, 0, currentDate, false);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dueDate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );
        service
          .find(123)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a MembershipFee', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dueDate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            dueDate: currentDate
          },
          returnedFromService
        );
        service
          .create(new MembershipFee())
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp.body));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a MembershipFee', () => {
        const returnedFromService = Object.assign(
          {
            amount: 1,
            dueDate: currentDate.format(DATE_FORMAT),
            isPaid: true
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dueDate: currentDate
          },
          returnedFromService
        );
        service
          .update(expected)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp.body));
        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of MembershipFee', () => {
        const returnedFromService = Object.assign(
          {
            amount: 1,
            dueDate: currentDate.format(DATE_FORMAT),
            isPaid: true
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            dueDate: currentDate
          },
          returnedFromService
        );
        service
          .query()
          .pipe(
            take(1),
            map(resp => resp.body)
          )
          .subscribe(body => (expectedResult = body));
        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a MembershipFee', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
