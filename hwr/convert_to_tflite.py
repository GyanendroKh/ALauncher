import tensorflow as tf


SAVED_MODEL_DIR = 'saved_model'
TFLITE_MODEL_FILE = 'model.tflite'

model = tf.keras.models.load_model(SAVED_MODEL_DIR)
model.summary()

converter = tf.lite.TFLiteConverter.from_keras_model(model)

tflite_model = converter.convert()

with open(TFLITE_MODEL_FILE, 'wb') as f:
    f.write(tflite_model)
