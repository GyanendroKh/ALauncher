from model import create_model
from os import path
from load_dataset import load_dataset, normalized_dataset
from dataset import IMAGE_SIZE
from tensorflow.keras.callbacks import LearningRateScheduler
import tensorflow as tf
import math


BATCH_SIZE = 64
EPOCHS = 5
AUTOTUNE = tf.data.AUTOTUNE

train_ds_dir = path.join('dataset', 'train')
val_ds_dir = path.join('dataset', 'test')
saved_model_dir = 'saved_model'

tfdata_train_ds = load_dataset(train_ds_dir, IMAGE_SIZE, BATCH_SIZE)
tfdata_val_ds = load_dataset(val_ds_dir, IMAGE_SIZE, BATCH_SIZE)

train_ds = normalized_dataset(tfdata_train_ds).cache().prefetch(buffer_size=AUTOTUNE)
val_ds = normalized_dataset(tfdata_val_ds).cache().prefetch(buffer_size=AUTOTUNE)

classes = tfdata_train_ds.class_names
num_classes = len(classes)

with open('classes.txt', 'w') as f:
    for c in classes:
        f.write(c + '\n')

IMAGE_SHAPE = (IMAGE_SIZE, IMAGE_SIZE, 1)


model = create_model(IMAGE_SHAPE, num_classes)
decay_callback = LearningRateScheduler(
    lambda epoch: 0.0001 + 0.02 * math.pow(1.0 / math.e, epoch / 3.0),
    verbose=1
)

model.compile(
    optimizer='adam',
    loss='sparse_categorical_crossentropy',
    metrics=['accuracy']
)
model.fit(
    train_ds,
    validation_data=val_ds,
    epochs=EPOCHS
)


model.save('model.h5')
tf.keras.models.save_model(model, saved_model_dir)
