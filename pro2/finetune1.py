import argparse
from pathlib import Path
from datasets import load_dataset
from transformers import (
    AutoTokenizer,
    AutoModelForCausalLM,
    DataCollatorForLanguageModeling,
    Trainer,
    TrainingArguments,
)

DEFAULT_MODEL = "distilgpt2"
DEFAULT_DATA_FILE = "data.txt"
DEFAULT_OUTPUT_DIR = "./gpt2_text_generator"
DEFAULT_MAX_LENGTH = 128


def parse_args():
    parser = argparse.ArgumentParser(
        description="Fine-tune GPT-2 on a custom text file and generate sample text."
    )
    parser.add_argument(
        "--model_name",
        default=DEFAULT_MODEL,
        help="Pretrained Hugging Face model name (default: distilgpt2)",
    )
    parser.add_argument(
        "--data_file",
        default=DEFAULT_DATA_FILE,
        help="Path to the training text file (default: data.txt)",
    )
    parser.add_argument(
        "--output_dir",
        default=DEFAULT_OUTPUT_DIR,
        help="Directory to save the fine-tuned model and tokenizer",
    )
    parser.add_argument(
        "--epochs",
        type=int,
        default=3,
        help="Number of training epochs (default: 3)",
    )
    parser.add_argument(
        "--batch_size",
        type=int,
        default=2,
        help="Batch size per device (default: 2)",
    )
    parser.add_argument(
        "--max_length",
        type=int,
        default=DEFAULT_MAX_LENGTH,
        help="Maximum token length for training examples and generation",
    )
    parser.add_argument(
        "--prompt",
        default="Artificial Intelligence is",
        help="Prompt text to generate from after training",
    )
    return parser.parse_args()


def main():
    args = parse_args()
    data_path = Path(args.data_file).resolve()
    output_path = Path(args.output_dir).resolve()

    if not data_path.exists():
        raise FileNotFoundError(f"Training file not found: {data_path}")

    print(f"Training file: {data_path}")
    print(f"Using model: {args.model_name}")
    print(f"Output directory: {output_path}\n")

    dataset = load_dataset("text", data_files={"train": str(data_path)})

    tokenizer = AutoTokenizer.from_pretrained(args.model_name)
    if tokenizer.pad_token is None:
        tokenizer.pad_token = tokenizer.eos_token

    model = AutoModelForCausalLM.from_pretrained(args.model_name)
    model.config.pad_token_id = tokenizer.eos_token_id

    print("Tokenizing dataset...")

    def tokenize_function(examples):
        return tokenizer(
            examples["text"],
            truncation=True,
            padding="max_length",
            max_length=args.max_length,
        )

    tokenized_dataset = dataset["train"].map(
        tokenize_function,
        batched=True,
        remove_columns=["text"],
    )

    data_collator = DataCollatorForLanguageModeling(
        tokenizer=tokenizer,
        mlm=False,
    )

    training_args = TrainingArguments(
        output_dir=str(output_path),
        overwrite_output_dir=True,
        num_train_epochs=args.epochs,
        per_device_train_batch_size=args.batch_size,
        save_strategy="epoch",
        logging_steps=10,
        report_to="none",
    )

    trainer = Trainer(
        model=model,
        args=training_args,
        train_dataset=tokenized_dataset,
        data_collator=data_collator,
    )

    print("Starting training...")
    trainer.train()

    output_path.mkdir(parents=True, exist_ok=True)
    print("Saving the fine-tuned model and tokenizer...")
    trainer.save_model(str(output_path))
    tokenizer.save_pretrained(str(output_path))

    print("Generating sample text...")
    generated = model.generate(
        tokenizer(args.prompt, return_tensors="pt").input_ids,
        max_new_tokens=80,
        do_sample=True,
        top_p=0.95,
        temperature=0.9,
        pad_token_id=tokenizer.eos_token_id,
    )

    print("\n=== Generated Text ===")
    print(tokenizer.decode(generated[0], skip_special_tokens=True))
    print("\nTraining complete.")


if __name__ == "__main__":
    main()