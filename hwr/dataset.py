import os
import gzip
import numpy as np
from PIL import Image

train_file = 'dataset/mnist/train-images-idx3-ubyte.gz'
train_label_file = 'dataset/mnist/train-labels-idx1-ubyte.gz'

MNIST_IMAGE_SIZE = 28


# How to decode http://yann.lecun.com/exdb/mnist/

def get_images(path: str, count: int) -> np.ndarray:
    with gzip.open(path, 'r') as f:
        f.read(16)  # header

        image_data = f.read(MNIST_IMAGE_SIZE * MNIST_IMAGE_SIZE * count)
        images = np.frombuffer(image_data, dtype=np.uint8)\
            .reshape((count, MNIST_IMAGE_SIZE, MNIST_IMAGE_SIZE))
        return images


def get_labels(path: str, count: int) -> np.ndarray:
    with gzip.open(path, 'r') as f:
        f.read(8)  # header

        label_data = f.read(count)
        labels = np.frombuffer(label_data, dtype=np.uint8)

        return labels


def save_as_jpg(img_file: str, label_file: str, save_dir: str) -> None:
    images = get_images(img_file, 10)
    labels = get_labels(label_file, 10)

    for idx in range(images.shape[0]):
        n_dir = os.path.join(save_dir, str(labels[idx]))

        if not os.path.exists(n_dir):
            os.makedirs(n_dir)

        img = Image.fromarray(images[idx])
        img.save(os.path.join(n_dir, f'{idx}.jpg'))


if __name__ == '__main__':
    save_as_jpg(train_file, train_label_file, os.path.join('dataset', 'mnist', 'train'))
