package fctreddit.clients.java;

import fctreddit.api.java.Image;
import fctreddit.api.java.Result;

public abstract class ImagesClient extends Client implements Image {

	abstract public Result<String> createImage(String userId, byte[] imageContents, String password);

	abstract public Result<byte[]> getImage(String userId, String imageId);

	abstract public Result<Void> deleteImage(String userId, String imageId, String password);

}
