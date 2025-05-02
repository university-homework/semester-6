import numpy as np


class LinearRegression:
    def __init__(self):
        self.a = None  # intercept
        self.b = None  # slope
        self.r = None  # correlation coefficient
        self.r_squared = None  # coefficient of determination

    def fit(self, x: np.ndarray, y: np.ndarray) -> None:
        """Calculate regression parameters using least squares method"""
        if len(x) != len(y):
            raise ValueError("Arrays x and y must have the same length")

        n = len(x)
        x_mean = np.mean(x)
        y_mean = np.mean(y)

        # Calculate covariance and variance
        cov_xy = np.sum((x - x_mean) * (y - y_mean)) / n
        var_x = np.sum((x - x_mean) ** 2) / n

        # Calculate regression coefficients
        self.b = cov_xy / var_x
        self.a = y_mean - self.b * x_mean

        # Calculate correlation coefficient
        std_x = np.std(x)
        std_y = np.std(y)
        self.r = cov_xy / (std_x * std_y)

        # Calculate coefficient of determination
        y_pred = self.a + self.b * x
        ss_res = np.sum((y - y_pred) ** 2)
        ss_tot = np.sum((y - y_mean) ** 2)
        self.r_squared = 1 - (ss_res / ss_tot)

    def predict(self, x: np.ndarray) -> np.ndarray:
        if self.a is None or self.b is None:
            raise ValueError("Model has not been fitted yet")
        return self.a + self.b * x

    def get_equation(self) -> str:
        return f"y = {self.a:.4f} + {self.b:.4f}*x"

    def get_results(self) -> dict:
        return {
            'intercept (a)': self.a,
            'slope (b)': self.b,
            'correlation coefficient (r)': self.r,
            'coefficient of determination (R²)': self.r_squared,
            'equation': self.get_equation()
        }


def input_data_manually() -> tuple[np.ndarray, np.ndarray]:
    print("Введите значения x через пробел:")
    x = np.array(list(map(float, input().split())))
    print("Введите соответствующие значения y через пробел:")
    y = np.array(list(map(float, input().split())))
    return x, y


def input_data_from_file(filename: str) -> tuple[np.ndarray, np.ndarray]:
    try:
        data = np.loadtxt(filename)
        if data.shape[1] != 2:
            raise ValueError("File must contain exactly 2 columns")
        return data[:, 0], data[:, 1]
    except Exception as e:
        print(f"Ошибка при чтении файла: {e}")
        exit()


def main():
    print("Программа линейной регрессии")
    print("Выберите способ ввода данных:")
    print("1 - Вручную")
    print("2 - Из файла")
    choice = input("Ваш выбор (1/2): ")

    if choice == '1':
        x, y = input_data_manually()
    elif choice == '2':
        filename = input("Введите имя файла: ")
        x, y = input_data_from_file(filename)
    else:
        print("Неверный выбор")
        return

    model = LinearRegression()
    model.fit(x, y)

    results = model.get_results()
    print("\nРезультаты:")
    for key, value in results.items():
        print(f"{key}: {value}")


if __name__ == "__main__":
    main()