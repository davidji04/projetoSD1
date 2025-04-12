package fctreddit.clients.java;

import fctreddit.api.java.Image;
import fctreddit.api.java.Result;

public abstract class ImageClient extends Client implements Image  {

	
	abstract public Result<String> createImage(byte[] imageContents);;

	abstract public Result<byte[]> getImage(String imageId);
	
	abstract public Result<Void> deleteImage(String imageId);

}
