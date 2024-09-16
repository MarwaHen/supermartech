import { Component } from '@angular/core';
import { TranslateDirective } from 'app/shared/language';

@Component({
  standalone: true,
  selector: 'jhi-footer',
  templateUrl: './footer.component.html',
  styleUrls: [
    '../../../content/hope/css/animate.css',
    '../../../content/hope/css/bootstrap.min.css',
    '../../../content/hope/css/font-awesome.min.css',
    '../../../content/hope/css/jquery-ui.css',
    '../../../content/hope/css/main.css',
    '../../../content/hope/css/meanmenu.min.css',
    '../../../content/hope/css/nivo-slider.css',
    '../../../content/hope/css/normalize.css',
    '../../../content/hope/css/owl.carousel.css',
    '../../../content/hope/css/owl.my_theme.css',
    '../../../content/hope/css/owl.theme.css',
    '../../../content/hope/css/owl.transitions.css',
    '../../../content/hope/css/responsive.css',
    '../../../content/hope/fancy-box/jquery.fancybox.css',
    '../../../content/hope/style.css',
  ],
  imports: [TranslateDirective],
})
export default class FooterComponent {}
