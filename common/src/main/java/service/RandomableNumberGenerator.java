package service;

public interface RandomableNumberGenerator<T extends Number> {
    T generateRandomNumber();
}
