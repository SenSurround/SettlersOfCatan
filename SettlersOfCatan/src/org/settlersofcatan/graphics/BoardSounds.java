package org.settlersofcatan.graphics;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.DataResource;

public interface BoardSounds extends ClientBundle {
  @Source("sounds/jackhammer-05.wav")
  DataResource jackhammerWav();
  
  @Source("sounds/jackhammer-05.mp3")
  DataResource jackhammerMp3();
  
  @Source("sounds/hammer.wav")
  DataResource hammerWav();
  
  @Source("sounds/hammer.mp3")
  DataResource hammerMp3();
  
  @Source("sounds/laugh.wav")
  DataResource laughWav();
  
  @Source("sounds/laugh.mp3")
  DataResource laughMp3();
}
