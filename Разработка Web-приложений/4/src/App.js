import { useState, useEffect } from 'react';
import axios from 'axios';
import { BrowserRouter as Router, Routes, Route, Link, useParams, Navigate } from 'react-router-dom';

const App = () => {
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  const handleLogin = () => {
    setIsAuthenticated(true);
  };

  const handleLogout = () => {
    setIsAuthenticated(false);
  };

  return (
    <Router>
      <div className="app">
        <nav className="navbar">
          <ul>
            <li>
              <Link to="/">Главная</Link>
            </li>
            <li>
              <Link to="/profile">Профиль</Link>
            </li>
            <li>
              <Link to="/products">Каталог товаров</Link>
            </li>
            <li>
              {isAuthenticated ? (
                <button onClick={handleLogout}>Выйти</button>
              ) : (
                <button onClick={handleLogin}>Войти</button>
              )}
            </li>
          </ul>
        </nav>

        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/products" element={<ProductCatalog />} />
          <Route path="/products/:id" element={<ProductDetails />} />
          <Route path="/not_authorized" element={<NotAuthorized />} />
          <Route
            path="/profile"
            element={
              isAuthenticated ? (
                <ProfilePage />
              ) : (
                <Navigate to="/not_authorized" replace />
              )
            }
          />
        </Routes>
      </div>
    </Router>
  );
};

const HomePage = () => {
  return (
    <div className="home-page">
      <h1>Добро пожаловать в наш магазин!</h1>
      <p>Используйте навигационное меню для просмотра товаров.</p>
    </div>
  );
};

const ProfilePage = () => {
  return (
    <div>
      <h1>Профиль</h1>
    </div>
  );
};

const NotAuthorized = () => {
  return (
    <div>
      <p>Эта страница доступна только авторизованным пользователям.</p>
    </div>
  );
};

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
      <Link to={`/products/${product.id}`} className="details-button">
        Подробнее
      </Link>
    </div>
  );
};

const ProductDetails = () => {
  const { id } = useParams();
  const [product, setProduct] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    axios.get(`https://fakestoreapi.com/products/${id}`)
      .then(response => {
        setProduct(response.data);
        setLoading(false);
      })
      .catch(err => {
        setError('Ошибка при загрузке товара');
        console.error(err);
        setLoading(false);
      });
  }, [id]);

  if (loading) {
    return <div>Загрузка данных о товаре...</div>;
  }

  if (error) {
    return <div>{error}</div>;
  }

  if (!product) {
    return <div>Товар не найден</div>;
  }

  return (
    <div className="product-details">
      <h1>{product.title}</h1>
      <div className="details-content">
        <img src={product.image} alt={product.title} className="product-image-large" />
        <div className="product-info">
          <p className="price">Цена: ${product.price}</p>
          <p className="category">Категория: {product.category}</p>
          <p className="rating">
            Рейтинг: {product.rating.rate} ({product.rating.count} отзывов)
          </p>
          <p className="full-description">{product.description}</p>
          <button className="add-to-cart">Добавить в корзину</button>
          <Link to="/products" className="back-button">
            Вернуться к каталогу
          </Link>
        </div>
      </div>
    </div>
  );
};

export default App;
