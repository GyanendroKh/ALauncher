import os
import gzip
from typing import Callable
import numpy as np
from PIL import Image

IMAGE_SIZE = 28


# How to decode http://yann.lecun.com/exdb/mnist/

def get_images(path: str, count: int) -> np.ndarray:
    with gzip.open(path, 'r') as f:
        f.read(16)  # header

        image_data = f.read(IMAGE_SIZE * IMAGE_SIZE * count)
        images = np.frombuffer(image_data, dtype=np.uint8)\
            .reshape((count, IMAGE_SIZE, IMAGE_SIZE))
        return images


def get_labels(path: str, count: int) -> np.ndarray:
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
    label_formart: Callable[[str], str] = lambda x: x,
    file_name_formart: Callable[[str], str] = lambda x: x,
) -> None:
    images = get_images(img_file, count)
    labels = get_labels(label_file, count)

    for idx in range(images.shape[0]):
        n_dir = os.path.join(save_dir, label_formart(str(labels[idx])))

        if not os.path.exists(n_dir):
            os.makedirs(n_dir)

        img = Image.fromarray(images[idx])
        func(img).save(os.path.join(n_dir, f'{file_name_formart(idx)}.jpg'))
