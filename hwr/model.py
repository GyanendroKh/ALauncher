from tensorflow.keras.layers import (
    Input,
    Conv2D,
    BatchNormalization,
    Activation,
    Dropout,
    Flatten,
    Dense
)
from tensorflow.keras.initializers import Constant
from tensorflow.keras.models import Model


def create_model(
    input_shape,
    num_classes: int
):
    inputs = Input(shape=input_shape, name='input')

    x = Conv2D(24, kernel_size=(6, 6), strides=1)(inputs)
    x = BatchNormalization(
        scale=False,
        beta_initializer=Constant(0.01)
    )(x)
    x = Activation('relu')(x)
    x = Dropout(rate=0.25)(x)

    x = Conv2D(48, kernel_size=(5, 5), strides=2)(x)
    x = BatchNormalization(
        scale=False,
        beta_initializer=Constant(0.01)
    )(x)
    x = Activation('relu')(x)
    x = Dropout(rate=0.25)(x)

    x = Conv2D(64, kernel_size=(4, 4), strides=2)(x)
    x = BatchNormalization(
        scale=False,
        beta_initializer=Constant(0.01)
    )(x)
    x = Activation('relu')(x)
    x = Dropout(rate=0.25)(x)

    x = Flatten()(x)
    x = Dense(200)(x)
    x = BatchNormalization(
        scale=False,
        beta_initializer=Constant(0.01)
    )(x)
    x = Activation('relu')(x)
    x = Dropout(rate=0.25)(x)

    predications = Dense(
        num_classes,
        activation='softmax',
        name='output'
    )(x)

    model = Model(inputs=inputs, outputs=predications)

    return model
