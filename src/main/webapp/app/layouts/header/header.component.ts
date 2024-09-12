import { Component, OnInit } from '@angular/core';
import { ICategory } from 'app/entities/category/category.model';
import { CategoryService } from 'app/entities/category/service/category.service';
import { SubCategoryService } from 'app/entities/sub-category/service/sub-category.service';
import { ISubCategory } from 'app/entities/sub-category/sub-category.model';
import { CommonModule } from '@angular/common';

@Component({
  standalone: true,
  selector: 'jhi-header',
  templateUrl: './header.component.html',
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
  imports: [CommonModule],
})
export class HeaderComponent implements OnInit {
  categories: ICategory[] = [];
  subCategories: ISubCategory[] = [];
  activeCategoryId: number | null = null;

  constructor(
    private categoryService: CategoryService,
    private subCategoryService: SubCategoryService,
  ) {}

  ngOnInit(): void {
    this.loadCategories();
    this.loadSubCategories();
  }

  loadCategories(): void {
    this.categoryService.query().subscribe(response => {
      this.categories = response.body ?? [];
    });
  }

  loadSubCategories(): void {
    this.subCategoryService.query().subscribe(response => {
      this.subCategories = response.body ?? [];
      this.organizeSubCategories();
    });
  }

  organizeSubCategories(): void {
    this.categories.forEach(category => {
      category.subCategories = this.subCategories.filter(subCategory => subCategory.cat_id === category.id);
    });
  }

  showSubCategories(categoryId: number): void {
    this.activeCategoryId = categoryId;
  }

  hideSubCategories(): void {
    this.activeCategoryId = null;
  }
}
