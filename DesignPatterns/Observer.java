public interface WeatherObserver{
	void update(WeatherType weather);
}

public class NewsToday implements WeatherObserver{
	void update(WeatherType weather){
		// newsToday says its weather 
}
}

public class CNN implements WeatherObserver{
	void update(WeatherType weather){
		// CNN says its weather 
}
}

public class Subjects{
	Private weatherType currentWeather;
	Private final List<WeatherObserver> observers;
	Public Weather(){
		Observers = new ArrayList<>();
		currentWeather = WeatherType.SUNNY;
    }
}

public void addObserver(WeatherObserver obs) {
    observers.add(obs);
  }

  public void removeObserver(WeatherObserver obs) {
    observers.remove(obs);
  }


public void timePasses() {
    var enumValues = WeatherType.values();
    currentWeather = enumValues[(currentWeather.ordinal() + 1) % enumValues.length];
    LOGGER.info("The weather changed to {}.", currentWeather);
    notifyObservers();
  }

  private void notifyObservers() {
    for (var obs : observers) {
      obs.update(currentWeather);
    }
}

  public static void main(String[] args) {

    var weather = new Weather();
    weather.addObserver(new NewsToday());
    weather.addObserver(new CNN());

    
   weather.timePasses();
    weather.timePasses();
    weather.timePasses();
    weather.timePasses();
   
  }

