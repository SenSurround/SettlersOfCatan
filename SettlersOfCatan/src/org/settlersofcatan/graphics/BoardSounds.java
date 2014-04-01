package org.settlersofcatan.graphics;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.DataResource;

public interface BoardSounds extends ClientBundle {
  @Source("sounds/jackhammer-05.wav")
  DataResource jackhammerWav();
  
  @Source("sounds/jackhammer-05.mp3")
  DataResource jackhammerMp3();
}
