from os import error, path

from PIL import Image
from dataset import (
    get_mnist_label_metadata,
    save_as_jpg
)


def main():
    dataset_dir = 'dataset'
    dataset_types = ['mnist', 'emnist']
    dataset_type_file_name = {
        'mnist': {
            'train': 'train',
            'test': 't10k'
        },
        'emnist': {
            'train': 'emnist-letters-train',
            'test': 'emnist-letters-test',
        }
    }
    dataset_filename_format = {
        'images': '{}-images-idx3-ubyte.gz',
        'labels': '{}-labels-idx1-ubyte.gz'
    }

    for dt in dataset_types:
        d = path.join(dataset_dir, dt)

        for t in dataset_type_file_name[dt].keys():
            labels_path = path.join(d, dataset_filename_format['labels'].replace('{}', dataset_type_file_name[dt][t]))
            images_path = path.join(d, dataset_filename_format['images'].replace('{}', dataset_type_file_name[dt][t]))

            if not path.exists(labels_path):
                raise error("File '{}' does not exist".format(labels_path))

            if not path.exists(images_path):
                raise error("File '{}' does not exist".format(images_path))

            label_count = get_mnist_label_metadata(labels_path)

            def func(x: Image.Image) -> Image.Image:
                if dt == 'emnist':
                    return x.transpose(Image.FLIP_LEFT_RIGHT).rotate(90)
                return x

            def label_format(x: str) -> str:
                if dt == 'emnist':
                    return chr(64 + int(x))
                return x

            def file_name_format(x: str) -> str:
                return x

            save_as_jpg(
                images_path,
                labels_path,
                label_count,
                path.join(dataset_dir, t),
                func,
                label_format,
                file_name_format
            )


if __name__ == '__main__':
    main()
