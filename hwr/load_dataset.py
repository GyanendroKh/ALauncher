import matplotlib.pyplot as plt
from os import path
import tensorflow as tf
from dataset import IMAGE_SIZE


def load_dataset(dir_path: str, image_size: int, batch_size: int) -> tf.data.Dataset:
    image_shape = (image_size, image_size)
    train_ds = tf.keras.preprocessing.image_dataset_from_directory(
        dir_path,
        image_size=image_shape,
        batch_size=batch_size,
        color_mode='grayscale'
    )

    return train_ds


def normalized_dataset(dataset: tf.data.Dataset):
    normalization_layer = tf.keras.layers.experimental.preprocessing.Rescaling(1. / 255)

    return dataset.map(lambda x, y: (normalization_layer(x), y))


def _main():
    BATCH_SIZE = 64
    train_dir = path.join('dataset', 'train')

    train_ds = load_dataset(train_dir, IMAGE_SIZE, BATCH_SIZE)
    class_names = train_ds.class_names

    plt.figure(figsize=(10, 10))
    for images, labels in train_ds.take(1):
        print(images.shape)
        for i in range(BATCH_SIZE):
            plt.subplot(4, BATCH_SIZE // 4, i + 1)
            plt.imshow(images[i].numpy().astype("uint8"), cmap='gray')
            plt.title(class_names[labels[i]])
            plt.axis("off")

    plt.show()


if __name__ == '__main__':
    _main()
