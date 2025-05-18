import numpy as np

eps = 0.001

def f(x):
    x1, x2 = x
    return 11 * x1 ** 2 - 60 * x1 * x2 + 82 * x2 ** 2 + 2 * x1 - 8 * x2 + 9


def fGr(x):
    x1, x2 = x
    fs1 = 22 * x1 + 8 * x2 - 12
    fs2 = 8 * x1 + 4 * x2
    return np.array([fs1, fs2])


def findA(A, x, h):
    num = -np.dot(fGr(x), h)
    den = np.dot(A @ h, h)
    a = num / den
    return a


def fastest(A, x0):
    i = 1
    x = x0
    h = -fGr(x)
    while np.linalg.norm(h) > eps:
        a = findA(A, x, h)
        x = x + a * h
        h = -fGr(x)
        i += 1

    print("Начальная точка:", x0, "\nПолученная точка:", x, "\nКоличество шагов:", i, "\n")

A = np.array([[22, 8], [8, 4]])

print("Метод наискорейшего спуска")

x0_list = [
    np.array([1.9, -4]),
    np.array([50, 50]),
    np.array([1000000, -1000000])
]

for x0 in x0_list:
    fastest(A, x0)
