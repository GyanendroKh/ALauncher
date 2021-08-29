import os
import gzip
from typing import Callable
import numpy as np
from PIL import Image

IMAGE_SIZE = 28


# How to decode http://yann.lecun.com/exdb/mnist/

def get_mnist_image_metadata(path: str):
    with gzip.open(path, 'r') as f:
        f.read(4)  # magic number

        a = int.from_bytes(f.read(4), 'big')
        b = int.from_bytes(f.read(4), 'big')
        c = int.from_bytes(f.read(4), 'big')

        # (count, width, height)
        return (a, b, c)


def get_mnist_label_metadata(path: str):
    with gzip.open(path, 'r') as f:
        f.read(4)  # header

        a = int.from_bytes(f.read(4), 'big')

        return a


def get_mnist_images(path: str, count: int) -> np.ndarray:
    with gzip.open(path, 'r') as f:
        f.read(16)  # header

        image_data = f.read(IMAGE_SIZE * IMAGE_SIZE * count)
        images = np.frombuffer(image_data, dtype=np.uint8)\
            .reshape((count, IMAGE_SIZE, IMAGE_SIZE))
        return images


def get_mnist_labels(path: str, count: int) -> np.ndarray:
    with gzip.open(path, 'r') as f:
        f.read(8)  # header

        label_data = f.read(count)
        labels = np.frombuffer(label_data, dtype=np.uint8)

        return labels


def save_as_jpg(
    img_file: str,
    label_file: str,
    count: int,
    save_dir: str,
    func: Callable[[Image.Image], Image.Image] = lambda x: x,
    label_format: Callable[[str], str] = lambda x: x,
    file_name_format: Callable[[str], str] = lambda x: x,
) -> None:
    images = get_mnist_images(img_file, count)
    labels = get_mnist_labels(label_file, count)

    for idx in range(images.shape[0]):
        n_dir = os.path.join(save_dir, label_format(str(labels[idx])))

        if not os.path.exists(n_dir):
            os.makedirs(n_dir)

        img = Image.fromarray(images[idx])
        func(img).save(os.path.join(n_dir, f'{file_name_format(idx)}.jpg'))
