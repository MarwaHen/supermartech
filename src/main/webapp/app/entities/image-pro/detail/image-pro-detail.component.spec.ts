import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ImageProDetailComponent } from './image-pro-detail.component';

describe('ImagePro Management Detail Component', () => {
  let comp: ImageProDetailComponent;
  let fixture: ComponentFixture<ImageProDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ImageProDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./image-pro-detail.component').then(m => m.ImageProDetailComponent),
              resolve: { imagePro: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ImageProDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ImageProDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load imagePro on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ImageProDetailComponent);

      // THEN
      expect(instance.imagePro()).toEqual(expect.objectContaining({ id: 123 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
