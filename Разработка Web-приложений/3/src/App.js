import { useState, useEffect } from 'react';
import axios from 'axios';

const ProductCatalog = () => {
  const [products, setProducts] = useState([]);
  const [categories, setCategories] = useState([]);
  const [selectedCategory, setSelectedCategory] = useState('all');
  const [sortOption, setSortOption] = useState('default');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    axios.get('https://fakestoreapi.com/products/categories')
      .then(response => {
        setCategories(response.data);
      })
      .catch(err => {
        setError('Ошибка при загрузке категорий');
        console.error(err);
      });
  }, []);

  useEffect(() => {
    setLoading(true);
    setError(null);

    const url = selectedCategory === 'all' 
      ? 'https://fakestoreapi.com/products'
      : `https://fakestoreapi.com/products/category/${selectedCategory}`;

    axios.get(url)
      .then(response => {
        let sortedProducts = [...response.data];

        if (sortOption === 'price-asc') {
          sortedProducts.sort((a, b) => a.price - b.price);
        } else if (sortOption === 'price-desc') {
          sortedProducts.sort((a, b) => b.price - a.price);
        } else if (sortOption === 'rating') {
          sortedProducts.sort((a, b) => b.rating.rate - a.rating.rate);
        }
        
        setProducts(sortedProducts);
        setLoading(false);
      })
      .catch(err => {
        setError('Ошибка при загрузке товаров');
        console.error(err);
        setLoading(false);
      });
  }, [selectedCategory, sortOption]);

  const handleCategoryChange = (e) => {
    setSelectedCategory(e.target.value);
  };

  const handleSortChange = (e) => {
    setSortOption(e.target.value);
  };

  if (loading && products.length === 0) {
    return <div>Загрузка данных...</div>;
  }

  if (error) {
    return <div>{error}</div>;
  }

  return (
    <div className="product-catalog">
      <h1>Каталог товаров</h1>
      
      <div className="controls">
        <div className="filter-control">
          <label htmlFor="category">Категория:</label>
          <select id="category" value={selectedCategory} onChange={handleCategoryChange}>
            <option value="all">Все категории</option>
            {categories.map(category => (
              <option key={category} value={category}>
                {category}
              </option>
            ))}
          </select>
        </div>
        
        <div className="sort-control">
          <label htmlFor="sort">Сортировка:</label>
          <select id="sort" value={sortOption} onChange={handleSortChange}>
            <option value="default">По умолчанию</option>
            <option value="price-asc">По цене (возрастание)</option>
            <option value="price-desc">По цене (убывание)</option>
            <option value="rating">По рейтингу</option>
          </select>
        </div>
      </div>
      
      <div className="product-list">
        {products.map(product => (
          <ProductCard key={product.id} product={product} />
        ))}
      </div>
    </div>
  );
};

const ProductCard = ({ product }) => {
  return (
    <div className="product-card">
      <img src={product.image} alt={product.title} className="product-image" />
      <h3>{product.title}</h3>
      <p className="price">${product.price}</p>
      <p className="rating">Рейтинг: {product.rating.rate} ({product.rating.count} отзывов)</p>
      <p className="description">{product.description.substring(0, 100)}...</p>
      <button className="add-to-cart">Добавить в корзину</button>
    </div>
  );
};

export default ProductCatalog;
