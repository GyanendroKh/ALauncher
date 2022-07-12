import tensorflow as tf

from dataset import IMAGE_SIZE
from matplotlib import pyplot as plt

model = tf.keras.models.load_model('model.h5')

with open('classes.txt', 'r') as f:
    classes = f.readlines()


p = 'dataset/test/Z/20000.jpg'

img = tf.keras.utils.load_img(p, target_size=(IMAGE_SIZE, IMAGE_SIZE), grayscale=True)
img = tf.keras.utils.img_to_array(img) / 255.0

prediction = model.predict(img.reshape(1, IMAGE_SIZE, IMAGE_SIZE, 1))

pred = tf.argmax(prediction[0])
print(pred)
print(classes[pred])

plt.imshow(img, cmap='gray')
plt.show()
