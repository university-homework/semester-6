def fibonacci(a, b, N, f):
    F = [1, 1]

    while len(F) < N + 1:
        F.append(F[-1] + F[-2])

    d = (b - a) / F[N] * 0.001
    error = 1 / 2 * ((b - a) / F[N] + d)

    x1 = a + F[N - 2] / F[N] * (b - a)
    x2 = a + F[N - 1] / F[N] * (b - a)

    f1 = f(x1)
    f2 = f(x2)

    for i in range(N - 3):
        if f1 <= f2:
            b = x2
            x2 = x1
            f2 = f1
            x1 = a + (b - x1)
            f1 = f(x1)
        else:
            a = x1
            x1 = x2
            f1 = f2
            x2 = a + (b - x1)
            f2 = f(x2)

    if f1 <= f2:
        b = x1
        x2 = x1 + d
        f2 = f(x2)
    else:
        a = x1
        x1 = x2
        f1 = f2
        x2 = x1 + d
        f2 = f(x2)

    if f1 <= f2:
        b = x1
    else:
        a = x1

    xa = (a + b) / 2
    return xa, error
