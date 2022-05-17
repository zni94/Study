import java.util.*;

public class CarRacing {

    public static void main(String[] args){
        List<Car> carList = new ArrayList<Car>();

        Integer carSize = inputSize();

        do {
            Car car = inputCar();
            if (car.isBlank(car.carName) || car.isOverName(car.carName)) break;

            carList.add(car);
        } while (carList.size() < carSize);

        if (carList.size() == carSize) {
            racingStart(carList);
        }
    }

    public static void racingStart(List<Car> carList) {
        System.out.println("RACING START !!\n");

        Integer racingSize = 5;

        for (int i = 0; i < racingSize; i++) playingRace(carList);

    }

    public static void playingRace(List<Car> carList) {
        carList.forEach((car) -> {
            Integer diceNum = Math.toIntExact(Math.round(Math.random() * 10));
            car.moveCar(car, diceNum, car.currentLocation);
        });

        System.out.println();
    }

    public static Integer inputSize() {
        Integer $carSize = 0;

        Scanner scanner = new Scanner(System.in);
        System.out.print("자동차의 수를 입력하세요 :: ");
        $carSize = scanner.nextInt();

        return $carSize;
    }

    public static Car inputCar() {
        Car car = new Car();

        String $carName = "";
        Integer $currentLocation = 0;

        Scanner scanner = new Scanner(System.in);
        System.out.print("자동차 이름 입력 :: ");
        $carName = scanner.nextLine();

        car.carName = $carName;
        car.currentLocation = $currentLocation;

        return car;
    }

    // 자동차 객체를 생성
    public static class Car {
        private Integer currentLocation;
        private String carName;

        public Car() {
            super();
        }

        public Car(String carName, Integer currentLocation) {
            this.carName = carName;
            this.currentLocation = currentLocation;
        }

        public boolean isBlank(String carName) {
            boolean $returnBool = false;

            if (carName.contains(" ") || carName.isBlank()) {
                System.out.println("이름에는 공백이 포함될 수 없습니다.");
                $returnBool = true;
            }

            return $returnBool;
        }

        public boolean isOverName(String carName) {
            boolean $returnBool = false;

            String[] car = carName.split("");
            if (car.length > 5) {
                System.out.println("이름은 5자를 초과할 수 없습니다.");
                $returnBool = true;
            }

            return $returnBool;
        }

        public void moveCar(Car car, Integer diceNum, Integer currentLocation) {
            car.currentLocation = diceNum > 3 ? printCar(car, currentLocation, true) : printCar(car, currentLocation, false);
        }

        public String returnIcon(Integer currentLocation) {
            String $returnIcon = "";

            for (int i = 0; i < currentLocation; i++) $returnIcon += "▶";

            return $returnIcon;
        }

        public Integer printCar(Car car, Integer currentLocation, boolean type) {
            if (type) currentLocation += 1;
            String move = returnIcon(currentLocation);

            System.out.println(move + car.carName);

            return currentLocation;
        }
    }
}