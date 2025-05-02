from lab_2.parts import a, b, c
from utils import math as utils_math


def main():
    intervals = [
        ('менее 500', 58),
        ('500-1000', 96),
        ('1000-1500', 239),
        ('1500-2000', 328),
        ('2000-2500', 147),
        ('более 2500', 132),
    ]
    total_population = 20000
    sample_size = 1000

    midpoints = [250, 750, 1250, 1750, 2250, 2750]
    frequencies = [freq for _, freq in intervals]

    sample_mean = utils_math.get_sample_mean(midpoints, frequencies, sample_size)
    sample_var = utils_math.get_sample_var(midpoints, frequencies, sample_size, sample_mean)
    fcp, std_error = utils_math.get_fcp(total_population, sample_size, sample_var)

    a(std_error)
    b(std_error, sample_mean)
    c(total_population, sample_var, std_error)


if __name__ == '__main__':
    main()
